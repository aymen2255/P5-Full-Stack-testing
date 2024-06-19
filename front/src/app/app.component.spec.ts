import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';


describe('AppComponent', () => {
  let component: AppComponent;
  let sessionService: SessionService;
  let router: Router;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();

  });


  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return an observable for isLogged', (done) => {
    sessionService['isLoggedSubject'] = new BehaviorSubject<boolean>(false);

    sessionService.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);

      sessionService['isLoggedSubject'].next(true);

      sessionService.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true);
        done();
      });
    });
  });

  it('should log out and navigate to home', () => {
    const logOutSpy = jest.spyOn(sessionService, 'logOut');
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.logout();

    expect(logOutSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
