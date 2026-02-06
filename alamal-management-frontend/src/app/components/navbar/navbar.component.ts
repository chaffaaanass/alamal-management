import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  isLoggedIn: boolean = false;
  menuState: 'opened' | 'closed' = 'closed';

  constructor(public authService: AuthService, private router: Router) {}

  isAuthenticated(): boolean {
    return this.authService.isLoggedIn();
  }

  toggleMenu() {
    this.menuState = this.menuState === 'closed' ? 'opened' : 'closed';
    console.log('Menu state:', this.menuState);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['']);
  }
}
