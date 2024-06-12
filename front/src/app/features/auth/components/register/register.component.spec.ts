import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, flush } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register with success', () => {
    expect(component.onError).toBeFalsy();// S'assurer que onError est false initialement

    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'Test',
      lastName: 'User',
      password: 'Test123!'
    };
    component.form.setValue(registerRequest); // Fournir des données de test pour le formulaire


    const spyAuthService = jest.spyOn(authService, "register").mockImplementation(() => of(undefined));

    const spyRouter = jest.spyOn(router, "navigate").mockImplementation(async () => true);

    component.submit();

    expect(spyAuthService).toHaveBeenCalledWith(registerRequest);
    expect(spyRouter).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false); // Vérifier que onError est toujours false

  });

  it('should trigger a validation error for email', () => {
    const registerRequest: RegisterRequest = {
      email: '',
      firstName: 'Test',
      lastName: 'User',
      password: 'Test123!'
    };

    component.form.setValue(registerRequest);

    fixture.detectChanges();
    component.submit();
    expect(component.form.get('email')?.errors?.['required']).toBeTruthy();
  });

  it('should trigger a validation error for firstName', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: '',
      lastName: 'User',
      password: 'Test123!'
    };

    component.form.setValue(registerRequest);

    fixture.detectChanges();
    component.submit();
    expect(component.form.get('firstName')?.errors?.['required']).toBeTruthy();

  });

  it('should trigger a validation error for lastName', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'test',
      lastName: '',
      password: 'Test123!'
    };

    component.form.setValue(registerRequest);

    fixture.detectChanges();
    component.submit();
    expect(component.form.get('lastName')?.errors?.['required']).toBeTruthy();

  });

  it('should trigger a validation error for password', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'test',
      lastName: 'test',
      password: ''
    };

    component.form.setValue(registerRequest);

    fixture.detectChanges();
    component.submit();
    expect(component.form.get('password')?.errors?.['required']).toBeTruthy();

  });

  it('should display  error when register fail', () => {
 
    const spyAuthService  = jest.spyOn(authService, "register").mockImplementation(throwError);

    component.submit();

    expect(component.onError).toBeTruthy;

  });

});
