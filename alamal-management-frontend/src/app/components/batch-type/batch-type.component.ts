import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { BatchTypeRequest } from '../../models/society/batch-type-request';
import { BatchType } from '../../models/society/batch-type';
import { BatchTypeService } from '../../services/batch-type/batch-type.service';

@Component({
  selector: 'app-batch-type',
  imports: [CommonModule, FormsModule],
  templateUrl: './batch-type.component.html',
  styleUrl: './batch-type.component.css',
})
export class BatchTypeComponent implements OnInit {
  batchTypes: BatchType[] = [];
  filteredTypes: BatchType[] = [];
  showModal: boolean = false;
  isEditMode: boolean = false;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Search filters
  searchType: string = '';

  currentBatchTypeId: number | null = null;

  // Form data
  batchTypeForm: BatchTypeRequest = {
    type: '',
    section: null,
  };

  constructor(
    private batchTypeService: BatchTypeService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadBatchTypes();
  }

  loadBatchTypes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.batchTypeService.getAllBatchTypes().subscribe({
      next: (data) => {
        this.batchTypes = data;
        this.filteredTypes = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to load batch types';
        console.error(error);
        this.loading = false;
      },
    });
  }

  filterBatchTypes(): void {
    this.filteredTypes = this.batchTypes.filter((type) => {
      const matchesType =
        !this.searchType ||
        type.type.toLowerCase().includes(this.searchType.toLowerCase());
      return matchesType;
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.showModal = true;
    this.resetForm();
  }

  openEditModal(batchType: BatchType): void {
    this.isEditMode = true;
    this.showModal = true;
    this.currentBatchTypeId = batchType.batchTypeId;

    this.batchTypeForm = {
      type: batchType.type,
      section: batchType.section,
    };
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  resetForm(): void {
    this.batchTypeForm = {
      type: '',
      section: null,  
    };
    this.currentBatchTypeId = null;
  }

  submitForm(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.currentBatchTypeId) {
      this.batchTypeService
        .updateBatchType(this.currentBatchTypeId, this.batchTypeForm)
        .subscribe({
          next: (response) => {
            this.successMessage = 'Batch type updated successfully';
            this.loadBatchTypes();
            this.loading = false;
            setTimeout(() => this.closeModal(), 1500);
          },
          error: (error) => {
            this.errorMessage =
              error.error?.message || 'Failed to update engineer type';
            console.error(error);
            this.loading = false;
          },
        });
    } else {
      this.batchTypeService.createBatchType(this.batchTypeForm).subscribe({
        next: (response) => {
          this.successMessage = 'Batch type created successfully';
          this.loadBatchTypes();
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

  deleteBatchType(batchTypeId: number): void {
    if (confirm('Are you sure you want to delete this batch type?')) {
      this.loading = true;

      this.batchTypeService.deleteBatchType(batchTypeId).subscribe({
        next: () => {
          this.successMessage = 'Batch type deleted successfully';
          this.loadBatchTypes();
          this.loading = false;
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to delete batch type';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  validateForm(): boolean {
    if (
      !this.batchTypeForm.type ||
      this.batchTypeForm.type.trim() === ''
    ) {
      this.errorMessage = 'Batch type is required';
      return false;
    }
    return true;
  }
}
