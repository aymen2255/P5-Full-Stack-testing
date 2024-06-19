import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { BehaviorSubject } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in the user and call next', () => {
    const sessionInformationMock :SessionInformation = {     
      token: 'string',
      type: 'string',
      id: 1,
      username: 'string',
      firstName: 'string',
      lastName: 'string',
      admin: false,    
    }
    const isLoggedSubjectSpy = jest.spyOn(service['isLoggedSubject'], 'next'); // Espionner la méthode next de isLoggedSubject
    service.logIn(sessionInformationMock );
    expect(service.sessionInformation).toEqual(sessionInformationMock);
    expect(service.isLogged).toBe(true);
    expect(isLoggedSubjectSpy).toHaveBeenCalledWith(true);
  });

 
  it('should log out the user and call next', () => {
    const isLoggedSubjectSpy = jest.spyOn(service['isLoggedSubject'], 'next'); // Espionner la méthode next de isLoggedSubject

    // Initialiser les valeurs avant l'appel de logOut
    service.sessionInformation = {     
      token: 'string',
      type: 'string',
      id: 1,
      username: 'string',
      firstName: 'string',
      lastName: 'string',
      admin: false,    
    }
    service.isLogged = true;

    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);
    expect(isLoggedSubjectSpy).toHaveBeenCalledWith(false);
  });

  it('should return an observable for isLogged', (done) => {
    service['isLoggedSubject'] = new BehaviorSubject<boolean>(false);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);

      service['isLoggedSubject'].next(true);

      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true);
        done();
      });
    });
  });

});
