import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: { login: jest.fn() } },
        { provide: Router, useValue: { navigate: jest.fn() } },
        { provide: SessionService, useValue: { logIn: jest.fn() } }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render login form', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const form = compiled.querySelector('form');
  
    expect(form).toBeTruthy();
  });

  it('should login successfully and navigate to /sessions', () => {
    
    const spyAuthService = jest.spyOn(authService, "login").mockImplementation(() => of({} as SessionInformation));   
    const spyRouter = jest.spyOn(router, "navigate").mockImplementation(async () => true);

    component.submit();

    expect(spyAuthService).toHaveBeenCalled();
    expect(spyRouter).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();

  });

  it('should handle authentication failure', () => {

    const error$ = throwError(() => new Error('An error occurred'));
    const spyAuthService =jest.spyOn(authService, 'login').mockReturnValue(error$);
  
    component.submit();

    fixture.detectChanges();
    const errorMessageElement = fixture.nativeElement.querySelector('p.error');    
    expect(errorMessageElement.textContent).toContain('An error occurred');
    expect(component.onError).toBeTruthy();
  });

  it('should require email and password', () => {
    let email = component.form.controls['email'];
    expect(email.valid).toBeFalsy();

    email.setValue('invalid-email'); // Invalid email
    expect(email.valid).toBeFalsy();

    email.setValue(''); // Invalid email
    expect(email.valid).toBeFalsy();

    email.setValue('toto@gmail.com'); // valid email
    expect(email.valid).toBeTruthy();


    let password = component.form.controls['password'];
    expect(password.valid).toBeFalsy();

    password.setValue('my password');
    expect(password.valid).toBeTruthy();

    password.setValue('');
    expect(password.valid).toBeFalsy();
    
  });



});
