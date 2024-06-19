import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, flush, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals'; 
import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { Router } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let matSnackBar: MatSnackBar;
  let sessionService: SessionService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    userService = TestBed.inject(UserService);
    matSnackBar = TestBed.inject(MatSnackBar);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on ngOnInit', () => {
    const mockUser:User = {
      id: 1,
      email:'toto@toto.com',
      lastName: 'testuser',
      firstName: 'Test',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    const userServiceSpy = jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
  
    component.ngOnInit();

    expect(userServiceSpy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('should go back', () => {

    const historyBackSpy = jest.spyOn(window.history, 'back');

    component.back();

    expect(historyBackSpy).toHaveBeenCalled();

  });

  it('should delete user account and logout on delete', fakeAsync(() => {
    // Mock des méthodes et des implémentations
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const logOutSpy = jest.spyOn(sessionService, 'logOut');
    const navigateSpy = jest.spyOn(router, 'navigate');
    const userServiceSpy = jest.spyOn(userService, "delete").mockReturnValue(of(null));

    // Appeler la méthode delete
     component.delete();
     tick(); // simulate the passage of time for async operations
     flush(); // flush pending timers
    // Vérifier que delete a été appelé avec le bon ID
    expect(userServiceSpy).toHaveBeenCalledWith('1');

    // Vérifier que matSnackBar.open a été appelé avec le bon message
    expect(snackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
   
    // Vérifier que sessionService.logOut a été appelé
    expect(logOutSpy).toHaveBeenCalled();

    // Vérifier que router.navigate a été appelé avec le bon chemin
    expect(navigateSpy).toHaveBeenCalledWith(['/']);

  }));

});
