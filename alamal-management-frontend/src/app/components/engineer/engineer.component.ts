import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EngineerRequest } from '../../models/engineer/engineer-request';
import { EngineerResponse } from '../../models/engineer/engineer-response';
import { EngineerService } from '../../services/engineer/engineer.service';
import { EngineerTypeService } from '../../services/engineer-type/engineer-type.service';
import { AuthService } from '../../services/auth/auth.service';
import { EngineerGroup } from '../../models/engineer/engineer-group';
import { EngineerSummary } from '../../models/engineer/engineer-summary';

@Component({
  selector: 'app-engineer',
  imports: [CommonModule, FormsModule],
  templateUrl: './engineer.component.html',
  styleUrl: './engineer.component.css',
})
export class EngineerComponent implements OnInit {
  engineers: EngineerResponse[] = [];
  filteredEngineers: EngineerResponse[] = [];
  groupedEngineers: Record<string, EngineerGroup> = {};
  engineerSummaryData: EngineerSummary | null = null;
  showModal: boolean = false;
  showSummaryModal: boolean = false;
  isEditMode: boolean = false;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  sortDescending: boolean = true;

  // Search filters
  searchName: string = '';
  searchType: string = '';
  searchDate: string = '';
  searchChequeNumber: number = 0;
  engineerTypeValues: string[] = [];

  // Form data
  engineerForm: EngineerRequest = {
    chequeNumber: 0,
    engineerName: '',
    amount: 0,
    chequeDate: '',
    acts: [],
    engineerType: '',
    createdBy: '',
  };

  currentEngineerId: number | null = null;
  newAct: number = 0;

  constructor(
    private engineerService: EngineerService,
    private engineerTypeService: EngineerTypeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadEngineers();
    this.loadEngineerTypes();
  }

  loadEngineers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.engineerService.getAllEngineers().subscribe({
      next: (engineersByName) => {
        this.engineers = engineersByName.flat();
        this.filteredEngineers = this.engineers;

        this.applySort();
        this.groupEngineersByName();

        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to load engineers';
        console.error(error);
        this.loading = false;
      },
    });
  }

  applySort(): void {
    const direction = this.sortDescending ? -1 : 1;

    this.filteredEngineers.sort(
      (a, b) => direction * (a.engineerId - b.engineerId)
    );
  }

  groupEngineersByName(): void {
    this.groupedEngineers = this.filteredEngineers.reduce((acc, engineer) => {
      const name = engineer.engineerName;

      if (!acc[name]) {
        acc[name] = {
          rows: [],
          acts: [],
        };
      }

      acc[name].rows.push(engineer);

      if (engineer.acts && engineer.acts.length > 0) {
        acc[name].acts.push(...engineer.acts);
      }

      return acc;
    }, {} as Record<string, EngineerGroup>);
  }

  toggleSort(): void {
    this.sortDescending = !this.sortDescending;

    const direction = this.sortDescending ? -1 : 1;

    this.filteredEngineers = [...this.filteredEngineers].sort(
      (a, b) => direction * (a.engineerId - b.engineerId)
    );

    this.groupEngineersByName();
  }

  getActsByEngineer(engineerName: string): number[] {
    return this.groupedEngineers[engineerName]?.acts ?? [];
  }

  onEngineerTypeChange(): void {
    this.loadEngineerTypes();
  }

  loadEngineerTypes(): void {
    this.engineerTypeService.getAllEngineerTypes().subscribe({
      next: (data) => {
        this.engineerTypeValues = data.map((type) => type.engineerType);
      },
      error: (error) => {
        console.error('Failed to load engineer types', error);
      },
    });
  }

  filterEngineers(): void {
    this.filteredEngineers = this.engineers.filter((engineer) => {
      const matchesName =
        !this.searchName ||
        engineer.engineerName
          .toLowerCase()
          .includes(this.searchName.toLowerCase());
      const matchesType =
        !this.searchType ||
        engineer.engineerType
          .toLowerCase()
          .includes(this.searchType.toLowerCase());
      const matchesDate =
        !this.searchDate || engineer.chequeDate === this.searchDate;
      const matchesChequeNumber =
        !this.searchChequeNumber ||
        engineer.chequeNumber === this.searchChequeNumber;

      return matchesName && matchesType && matchesDate && matchesChequeNumber;
    });
  }

  objectKeys = Object.keys;

  engineerSummary(name: string): void {
    this.engineerService.getEngineerSummary(name).subscribe({
      next: (summary) => {
        this.engineerSummaryData = summary;
      },
      error: (error) => {
        console.error('Failed to get engineer summary', error);
        this.engineerSummaryData = null;
      },
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.showModal = true;
    this.resetForm();
  }

  openSummaryModal(): void {
    this.showSummaryModal = true;
  }

  openEditModal(engineer: EngineerResponse): void {
    this.isEditMode = true;
    this.showModal = true;
    this.currentEngineerId = engineer.engineerId;

    this.engineerForm = {
      chequeNumber: engineer.chequeNumber,
      engineerName: engineer.engineerName,
      amount: engineer.amount,
      chequeDate: engineer.chequeDate,
      acts: [...engineer.acts],
      engineerType: engineer.engineerType,
      createdBy: engineer.createdBy,
    };
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  closeSummaryModal(): void {
    this.showSummaryModal = false;
    this.errorMessage = '';
    this.successMessage = '';
  }

  resetForm(): void {
    this.engineerForm = {
      chequeNumber: 0,
      engineerName: '',
      amount: 0,
      chequeDate: '',
      acts: [],
      engineerType: '',
      createdBy: '',
    };
    this.currentEngineerId = null;
    this.newAct = 0;
  }

  addAct(): void {
    if (this.newAct > 0) {
      this.engineerForm.acts.push(this.newAct);
      this.newAct = 0;
    }
  }

  removeAct(index: number): void {
    this.engineerForm.acts.splice(index, 1);
  }

  submitForm(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.currentEngineerId) {
      this.engineerService
        .updateEngineer(this.currentEngineerId, this.engineerForm)
        .subscribe({
          next: (response) => {
            this.successMessage = 'Engineer updated successfully';
            this.loadEngineers();
            this.loading = false;
            setTimeout(() => this.closeModal(), 1500);
          },
          error: (error) => {
            this.errorMessage =
              error.error?.message || 'Failed to update engineer';
            console.error(error);
            this.loading = false;
          },
        });
    } else {
      this.engineerForm.createdBy = this.authService.getUsername()!;
      this.engineerService.createEngineer(this.engineerForm).subscribe({
        next: (response) => {
          this.successMessage = 'Engineer created successfully';
          this.loadEngineers();
          this.loading = false;
          setTimeout(() => this.closeModal(), 1500);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to create engineer';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  deleteEngineer(engineerId: number): void {
    if (confirm('Are you sure you want to delete this engineer?')) {
      this.loading = true;

      this.engineerService.deleteEngineer(engineerId).subscribe({
        next: () => {
          this.successMessage = 'Engineer deleted successfully';
          this.loadEngineers();
          this.loading = false;
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to delete engineer';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  validateForm(): boolean {
    if (
      !this.engineerForm.engineerName ||
      this.engineerForm.engineerName.trim() === ''
    ) {
      this.errorMessage = 'Engineer name is required';
      return false;
    }
    if (this.engineerForm.chequeNumber <= 0) {
      this.errorMessage = 'Valid cheque number is required';
      return false;
    }
    if (this.engineerForm.amount <= 0) {
      this.errorMessage = 'Valid amount is required';
      return false;
    }
    if (!this.engineerForm.chequeDate) {
      this.errorMessage = 'Cheque date is required';
      return false;
    }
    if (
      !this.engineerForm.engineerType ||
      this.engineerForm.engineerType.trim() === ''
    ) {
      this.errorMessage = 'Engineer type is required';
      return false;
    }
    return true;
  }
}
