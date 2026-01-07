import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EngineerType } from '../../models/engineer/engineer-type';
import { EngineerTypeService } from '../../services/engineer-type/engineer-type.service';
import { AuthService } from '../../services/auth/auth.service';
import { EngineerTypeRequest } from '../../models/engineer/engineer-type-request';

@Component({
  selector: 'app-engineer-type',
  imports: [CommonModule, FormsModule],
  templateUrl: './engineer-type.component.html',
  styleUrl: './engineer-type.component.css',
})
export class EngineerTypeComponent implements OnInit {
  engineerTypes: EngineerType[] = [];
  filteredTypes: EngineerType[] = [];
  showModal: boolean = false;
  isEditMode: boolean = false;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Search filters
  searchType: string = '';

  currentEngineerTypeId: number | null = null;

  // Form data
  engineerTypeForm: EngineerTypeRequest = {
    engineerType: '',
  };

  constructor(
    private engineerTypeService: EngineerTypeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadEngineerTypes();
  }

  loadEngineerTypes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.engineerTypeService.getAllEngineerTypes().subscribe({
      next: (data) => {
        this.engineerTypes = data;
        this.filteredTypes = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to load engineers';
        console.error(error);
        this.loading = false;
      },
    });
  }

  filterEngineerTypes(): void {
    this.filteredTypes = this.engineerTypes.filter((type) => {
      const matchesType =
        !this.searchType ||
        type.engineerType.toLowerCase().includes(this.searchType.toLowerCase());

      return matchesType;
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.showModal = true;
    this.resetForm();
  }

  openEditModal(engineerType: EngineerType): void {
    this.isEditMode = true;
    this.showModal = true;
    this.currentEngineerTypeId = engineerType.engineerTypeId;

    this.engineerTypeForm = {
      engineerType: engineerType.engineerType,
    };
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  resetForm(): void {
    this.engineerTypeForm = {
      engineerType: '',
    };
    this.currentEngineerTypeId = null;
  }

  submitForm(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.currentEngineerTypeId) {
      this.engineerTypeService
        .updateEngineerType(this.currentEngineerTypeId, this.engineerTypeForm)
        .subscribe({
          next: (response) => {
            this.successMessage = 'Engineer type updated successfully';
            this.loadEngineerTypes();
            this.loading = false;
            setTimeout(() => this.closeModal(), 1500);
          },
          error: (error) => {
            this.errorMessage =
              error.error?.message || 'Failed to update engineer type';
            this.loading = false;
          },
        });
    } else {
      this.engineerTypeService.createEngineerType(this.engineerTypeForm).subscribe({
        next: (response) => {
          this.successMessage = 'Engineer type created successfully';
          this.loadEngineerTypes();
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

  deleteEngineer(engineerTypeId: number): void {
    if (confirm('Are you sure you want to delete this engineer?')) {
      this.loading = true;

      this.engineerTypeService.deleteEngineerType(engineerTypeId).subscribe({
        next: () => {
          this.successMessage = 'Engineer type deleted successfully';
          this.loadEngineerTypes();
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
      !this.engineerTypeForm.engineerType ||
      this.engineerTypeForm.engineerType.trim() === ''
    ) {
      this.errorMessage = 'Engineer type is required';
      return false;
    }
    return true;
  }
}
