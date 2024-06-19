import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Description de test',
    date: new Date(),
    teacher_id: 123,
    users: [1, 2, 3],
    createdAt: new Date(),
    updatedAt: new Date()
  };



  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with session data on ngOnInit when updating', () => {
    const initFormSpy = jest.spyOn(component as any, 'initForm'); // Accéder à la méthode privée via spy

    const sessionApiServiceSpy  = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    jest.spyOn(router, 'url', 'get').mockReturnValue('/update/1');

    // Appeler ngOnInit indirectement via fixture.detectChanges() ou directement
    component.ngOnInit();
    fixture.detectChanges();

    expect(initFormSpy).toHaveBeenCalled(); // Vérifier que initForm a été appelée
    expect(component.sessionForm).toBeTruthy();
    expect(component.sessionForm!.get('name')!.value).toBe('Test Session');
    expect(component.sessionForm!.get('date')!.value).toBe(new Date().toISOString().split('T')[0]);
    expect(component.sessionForm!.get('teacher_id')!.value).toBe(123);
    expect(component.sessionForm!.get('description')!.value).toBe('Description de test');

    sessionApiServiceSpy.mockRestore();
  });

  it('should initialize the form with empty values on ngOnInit when creating', () => {
    const initFormSpy = jest.spyOn(component as any, 'initForm'); // Accéder à la méthode privée via spy

    // Simuler le comportement attendu dans ngOnInit pour une création
    jest.spyOn(router, 'url', 'get').mockReturnValue('/create');

    // Appeler ngOnInit indirectement via fixture.detectChanges() ou directement
    component.ngOnInit();
    fixture.detectChanges();

    expect(initFormSpy).toHaveBeenCalled(); // Vérifier que initForm a été appelée
    expect(component.sessionForm).not.toBeNull(); // Vérifier que le formulaire n'est pas nul
    expect(component.sessionForm!.get('name')!.value).toBe(''); // Utilisation de ! pour indiquer que name et value ne sont pas nuls

    initFormSpy.mockRestore();
  });


  it('should call sessionApiService.update when onUpdate is true', () => {

    component.onUpdate = true;

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, "update").mockReturnValue(of(mockSession));

    const matSnackBarMock = jest.spyOn(matSnackBar, "open").mockImplementation();
    const routerSpy = jest.spyOn(router, "navigate").mockImplementation(async () => true);
    const exitPageSpy = jest.spyOn(component as any, 'exitPage');

    component.submit();

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(matSnackBarMock).toHaveBeenCalled();
    expect(exitPageSpy).toHaveBeenCalledWith('Session updated !');
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);

  });

  it('should call sessionApiService.create when onUpdate is false', () => {

    component.onUpdate = false;

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, "create").mockReturnValue(of(mockSession));

    const matSnackBarMock = jest.spyOn(matSnackBar, "open").mockImplementation();
    const routerSpy = jest.spyOn(router, "navigate").mockImplementation(async () => true);
    const exitPageSpy = jest.spyOn(component as any, 'exitPage');

    component.submit();

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(matSnackBarMock).toHaveBeenCalled();
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);

  });

});
