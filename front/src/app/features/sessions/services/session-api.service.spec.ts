import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu'il n'y a pas de requêtes HTTP en attente
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });



  it('should retrieve a list of sessions', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Session 1',
        description: 'Description de la session 1',
        date: new Date(),
        teacher_id: 10,
        users: [1, 2, 3],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Description de la session 2',
        date: new Date(),
        teacher_id: 15,
        users: [4, 5],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];
    // Souscrire à l'observable et vérifier les résultats
    service.all().subscribe((sessions: Session[]) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should return a session detail', () => {
    const mockSession: Session =  {
      id: 1,
      name: 'Session 1',
      description: 'Description de la session 1',
      date: new Date(),
      teacher_id: 10,
      users: [1, 2, 3],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.detail('1').subscribe((session: Session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });


  it('should create session', () => {
    const newSession: Session =  {
      id: 1,
      name: 'Session 1',
      description: 'Description de la session 1',
      date: new Date(),
      teacher_id: 10,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    service.create(newSession).subscribe((session: Session) => {
        expect(session).toEqual(newSession)
    });
      
    const req = httpMock.expectOne('api/session');
    
    expect(req.request.method).toEqual('POST');

    expect(req.request.body).toEqual(newSession);

    req.flush(newSession);
  });

  it('should update a session', () => {
    const mockUpdatedSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description de la session 1',
      date: new Date(),
      teacher_id: 1,
      users: [],
    };
    service.update("1", mockUpdatedSession).subscribe((session: Session) => {
      expect(session).toEqual(mockUpdatedSession);
    });
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockUpdatedSession);

  });

  it('should participate in session', () => {
    service.participate('1', '2').subscribe(res => {
      expect(res).toEqual({});
    });

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    req.flush({});

  });

  it('should unparticipate in session', () => {
    service.unParticipate('1', '2').subscribe(res => {
      expect(res).toEqual({});
    });

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should delete session', () => {
    service.delete('1').subscribe(res => {
      expect(res).toEqual({});
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

});
