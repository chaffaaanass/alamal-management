import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/auth/user';
import { AuthService } from '../../services/auth/auth.service';
import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-user',
  imports: [CommonModule, FormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  showModal: boolean = false;
  isEditMode: boolean = false;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  // Search filters
  searchUsername: string = '';

  currentUsername: string | null = null;

  // Form data
  userForm: User = {
    username: '',
    password: ''
  };

  constructor(
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.filteredUsers = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to load users';
        console.error(error);
        this.loading = false;
      },
    });
  }

  filterUsers(): void {
    this.filteredUsers = this.users.filter((user) => {
      const matchesUsername =
        !this.searchUsername ||
        user.username.toLowerCase().includes(this.searchUsername.toLowerCase());
      return matchesUsername;
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.showModal = true;
    this.resetForm();
  }

  openEditModal(user: User): void {
    this.isEditMode = true;
    this.showModal = true;
    this.currentUsername = user.username;

    this.userForm = {
      username: user.username,
      password: user.password
    };
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  resetForm(): void {
    this.userForm = {
      username: '',
      password: ''
    };
    this.currentUsername = null;
  }

  submitForm(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isEditMode && this.currentUsername) {
      this.userService.updateUser(this.currentUsername, this.userForm).subscribe({
        next: (response) => {
          this.successMessage = 'User updated successfully';
            this.loadUsers();
            this.loading = false;
            setTimeout(() => this.closeModal(), 1500);
          },
          error: (error) => {
            this.errorMessage =
              error.error?.message || 'Failed to update user';
            console.error(error);
            this.loading = false;
          },
        });
    } else {
      this.userService.createUser(this.userForm).subscribe({
        next: (response) => {
          this.successMessage = 'User created successfully';
          this.loadUsers();
          this.loading = false;
          setTimeout(() => this.closeModal(), 1500);
        },
        error: (error) => {
          this.errorMessage =
            error.error?.message || 'Failed to create user';
          console.error(error);
          this.loading = false;
        },
      });
    }
  }

  deleteUser(username: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.loading = true;

      this.userService.deleteUser(username).subscribe({
        next: () => {
          this.successMessage = 'User deleted successfully';
          this.loadUsers();
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
      !this.userForm.username ||
      this.userForm.username.trim() === ''
    ) {
      this.errorMessage = 'Username is required';
      return false;
    }
    if (
      !this.userForm.password ||
      this.userForm.password.trim() === ''
    ) {
      this.errorMessage = 'Password is required';
      return false;
    }
    return true;
  }
}
