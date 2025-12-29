import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SocietyRequest } from '../../models/society/society-request';
import { SocietyResponse } from '../../models/society/society-response';
import { SocietyService } from '../../services/society/society.service';
import { BatchTypeService } from '../../services/batch-type/batch-type.service';
import { SocietyGroup } from '../../models/society/society-group';
import { SocietySummary } from '../../models/society/society-summary';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-society',
  imports: [CommonModule, FormsModule],
  templateUrl: './society.component.html',
  styleUrl: './society.component.css',
})
export class SocietyComponent implements OnInit {
  societies: SocietyResponse[] = [];
  filteredSocieties: SocietyResponse[] = [];
  groupedSocieties: Record<string, SocietyGroup> = {};
  societySummaryData: SocietySummary | null = null;
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
  batchTypeValues: string[] = [];

  // Form data
  societyForm: SocietyRequest = {
    chequeNumber: 0,
    societyName: '',
    sum: 0,
    date: '',
    batches: [],
    batchType: '',
    section: 0,
    createdBy: '',
  };

  currentAttachmentNumber: number | null = null;
  newBatch: number = 0;

  constructor(
    private societyService: SocietyService,
    private batchTypeService: BatchTypeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadSocieties();
    this.loadBatchTypes();
  }

  loadSocieties(): void {
    this.loading = true;
    this.errorMessage = '';

    this.societyService.getAllSocieties().subscribe({
      next: (societiesByName) => {
        this.societies = societiesByName.flat();
        this.filteredSocieties = this.societies;

        this.applySort();
        this.groupSocietiesByName();

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

    this.filteredSocieties.sort(
      (a, b) => direction * (a.attachmentNumber - b.attachmentNumber)
    );
  }

  groupSocietiesByName(): void {
    this.groupedSocieties = this.filteredSocieties.reduce((acc, society) => {
      const name = society.societyName;

      if (!acc[name]) {
        acc[name] = {
          rows: [],
          batches: [],
        };
      }

      acc[name].rows.push(society);

      if (society.batches && society.batches.length > 0) {
        acc[name].batches.push(...society.batches);
      }

      return acc;
    }, {} as Record<string, SocietyGroup>);
  }

  toggleSort(): void {
    this.sortDescending = !this.sortDescending;

    const direction = this.sortDescending ? -1 : 1;

    this.filteredSocieties = [...this.filteredSocieties].sort(
      (a, b) => direction * (a.attachmentNumber - b.attachmentNumber)
    );

    this.groupSocietiesByName();
  }

  getBatchesBySociety(societyName: string): number[] {
    return this.groupedSocieties[societyName]?.batches ?? [];
  }

  onBatchTypeChange(): void {
    this.loadBatchTypes();
  }

  loadBatchTypes(): void {
    this.batchTypeService.getAllBatchTypes().subscribe({
      next: (data) => {
        this.batchTypeValues = data.map((type) => type.type);
      },
      error: (error) => {
        console.error('Failed to load batch types', error);
      },
    });
  }

  filterSocieties(): void {
    this.filteredSocieties = this.societies.filter((society) => {
      const matchesName =
        !this.searchName ||
        society.societyName
          .toLowerCase()
          .includes(this.searchName.toLowerCase());
      const matchesType =
        !this.searchType ||
        society.batchType
          .toLowerCase()
          .includes(this.searchType.toLowerCase());
      const matchesDate =
        !this.searchDate || society.date === this.searchDate;
      const matchesChequeNumber =
        !this.searchChequeNumber ||
        society.chequeNumber === this.searchChequeNumber;

      return matchesName && matchesType && matchesDate && matchesChequeNumber;
    });
  }

  objectKeys = Object.keys;

  societySummary(name: string): void {
    this.societyService.getSocietySummary(name).subscribe({
      next: (summary) => {
        this.societySummaryData = summary;
      },
      error: (error) => {
        console.error('Failed to get society summary', error);
        this.societySummaryData = null;
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

  openEditModal(society: SocietyResponse): void {
    this.isEditMode = true;
    this.showModal = true;
    this.currentAttachmentNumber = society.attachmentNumber;

    this.societyForm = {
      chequeNumber: society.chequeNumber,
      societyName: society.societyName,
      date: society.date,
      sum: society.sum,
      batches: [...society.batches],
      batchType: society.batchType,
      section: society.section,
      createdBy: society.createdBy,
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
    this.societyForm = {
      chequeNumber: 0,
      societyName: '',
      date: '',
      sum: 0,
      batches: [],
      batchType: '',
      section: 0,
      createdBy: '',
    };
    this.currentAttachmentNumber = null;
    this.newBatch = 0;
  }

  addBatch(): void {
    if (this.newBatch > 0) {
      this.societyForm.batches.push(this.newBatch);
      this.newBatch = 0;
    }
  }

  removeBatch(index: number): void {
    this.societyForm.batches.splice(index, 1);
  }

  submitForm(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.currentAttachmentNumber) {
      this.societyService
        .updateSociety(this.currentAttachmentNumber, this.societyForm)
        .subscribe({
          next: (response) => {
            this.successMessage = 'Society updated successfully';
            this.loadSocieties();
            this.loading = false;
            setTimeout(() => this.closeModal(), 1500);
          },
          error: (error) => {
            this.errorMessage =
              error.error?.message || 'Failed to update society';
            console.error(error);
            this.loading = false;
          },
        });
    } else {
      this.societyForm.createdBy = this.authService.getUsername()!;
      this.societyService.createSociety(this.societyForm).subscribe({
        next: (response) => {
          this.successMessage = 'Society created successfully';
          this.loadSocieties();
          this.loading = false;
          setTimeout(() => this.closeModal(), 1500);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to create society';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  deleteSociety(attachmentNumber: number): void {
    if (confirm('Are you sure you want to delete this society?')) {
      this.loading = true;

      this.societyService.deleteSociety(attachmentNumber).subscribe({
        next: () => {
          this.successMessage = 'Society deleted successfully';
          this.loadSocieties();
          this.loading = false;
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to delete society';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  validateForm(): boolean {
    if (
      !this.societyForm.societyName ||
      this.societyForm.societyName.trim() === ''
    ) {
      this.errorMessage = 'Society name is required';
      return false;
    }
    if (this.societyForm.chequeNumber <= 0) {
      this.errorMessage = 'Valid cheque number is required';
      return false;
    }
    if (this.societyForm.sum <= 0) {
      this.errorMessage = 'Valid sum is required';
      return false;
    }
    if (!this.societyForm.date) {
      this.errorMessage = 'Cheque date is required';
      return false;
    }
    if (
      !this.societyForm.batchType ||
      this.societyForm.batchType.trim() === ''
    ) {
      this.errorMessage = 'Batch type is required';
      return false;
    }
    return true;
  }
}
