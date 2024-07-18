import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let authService: AuthService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    authService = TestBed.inject(AuthService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu'il n'y a pas de requêtes HTTP en attente
    httpController.verify();
  });

  it('should send register request and receive a response', () => {
    const mockRegisterRequest: RegisterRequest = {
      email: 'test@gmail.com',
      firstName: 'firstName',
      lastName: 'lastName',
      password: 'password',
    };
  
    authService.register(mockRegisterRequest).subscribe(() => {});
  
    const req = httpController.expectOne(`${authService['pathService']}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRegisterRequest);
    req.flush(null); // Simuler une réponse vide du backend
  });

  it('should send login request and receive session information', () => {
    const mockLoginRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'password'
    };
  
    authService.login(mockLoginRequest).subscribe(sessionInfo => {
      expect(sessionInfo).toEqual(mockLoginRequest);
    });
  
    const req = httpController.expectOne(`${authService['pathService']}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginRequest);
    req.flush(mockLoginRequest); // Simuler une réponse vide du backend
  });
  
});


  