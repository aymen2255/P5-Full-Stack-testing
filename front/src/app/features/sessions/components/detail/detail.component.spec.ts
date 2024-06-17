import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { Session } from '../../interfaces/session.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { By } from '@angular/platform-browser';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let router: Router;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let teacherService: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    teacherService = TestBed.inject(TeacherService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call window.history.back() when back() is called', () => {
    const historyBackSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historyBackSpy).toHaveBeenCalled();
  });

  it('should delete session', () => {

    component.sessionId = '123';
    const sessionApiServiceMock = jest.spyOn(sessionApiService, "delete").mockReturnValue(of({}));

    const routerSpy = jest.spyOn(router, "navigate").mockResolvedValue(true);

    const snackBarSpy = jest.spyOn(matSnackBar, "open").mockReturnValue({} as any);

    component.delete();

    expect(sessionApiServiceMock).toHaveBeenCalledWith('123');
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);

  });

  it('should call participate in a session with sessionId and userId', () => {
    const participateSpy = jest.spyOn(sessionApiService, "participate").mockReturnValue(of());
       // Initialiser les valeurs de sessionId et userId
       component.userId = '456';
       component.sessionId = '123';       

    component.participate();
    expect(participateSpy).toHaveBeenCalledWith('123', '456');
  });

  it('should unparticipate session', () => {
    
    const unParticipateSpy = jest.spyOn(sessionApiService, "unParticipate").mockImplementation(() => of(undefined));

    component.unParticipate();

    expect(unParticipateSpy).toHaveBeenCalled();

  });

  it('should fetch session', () => {
    // Initialiser les valeurs nécessaires dans le composant
    component.sessionId = '123';

    // Données fictives pour simuler les réponses des services
    const sessionData: Session = {
      id: 123,
      name: 'Session Test',
      description: 'Description de la session',
      date: new Date(),
      teacher_id: 456,
      users: [1, 2, 3],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    const teacherData: Teacher = {
      id: 456,
      lastName: 'toto',
      firstName: "toto",
      createdAt: new Date(),
      updatedAt: new Date()
    };

    // Espionner les méthodes detail des services et simuler leurs retours
    const sessionDetailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(sessionData));
    const teacherDetailSpy = jest.spyOn(teacherService, 'detail').mockReturnValue(of(teacherData));

    // Appeler la méthode à tester
    component.ngOnInit();

    // Vérifier que sessionApiService.detail a été appelé avec this.sessionId
    expect(sessionDetailSpy).toHaveBeenCalledWith(sessionData.id!.toString());
    expect(teacherDetailSpy).toHaveBeenCalledWith(sessionData.teacher_id.toString());

    expect(component.session).toEqual(sessionData);
    expect(component.isParticipate).toBe(true);
    expect(component.teacher).toEqual(teacherData);
  });

  it('should not display delete button if user is not admin', () => {
    component.isAdmin = false; // Simuler un utilisateur non admin
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const deleteButton = compiled.querySelector('delete-button');
    expect(deleteButton).toBeNull(); // Le bouton ne devrait pas exister
  });

  it('should display delete button if user is admin', () => {
    component.isAdmin = true; // Simuler un utilisateur admin
   
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    const deleteButton = compiled.querySelector('delete-button');
    expect(deleteButton).toBeDefined(); // Le bouton devrait exister

  });

});

