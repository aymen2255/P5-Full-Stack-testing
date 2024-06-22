describe('List tests', () => {

    const loginAsAdmin = () => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName2',
                firstName: 'firstName2',
                lastName: 'lastName2',
                admin: true
            },
        });
        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
    };

    const interceptSessions = (sessions) => {
        cy.intercept('GET', '/api/session', {
            body: sessions
        });
    };

    const interceptTeachers = (teachers) => {
        cy.intercept('GET', '/api/teacher', {
            body: teachers
        });
    };

    const sessions = [{
        id: 1,
        name: "session1",
        date: "2024-04-05T08:00:00.000+00:00",
        teacher_id: 1,
        description: "session 1",
        users: [],
        createdAt: "2024-03-15T08:23:02",
        updatedAt: "2024-03-15T08:23:02"
    },
    {
        id: 2,
        name: "session2",
        date: "2024-04-05T08:00:00.000+00:00",
        teacher_id: 1,
        description: "session 2",
        users: [],
        createdAt: "2024-03-15T08:23:02",
        updatedAt: "2024-03-15T08:23:02"
    }];

    const teachers = [{
        id: 1,
        lastName: 'teacher1LastName',
        firstName: 'teacher1FirstName',
        createdAt: new Date(),
        updatedAt: new Date()
    },
    {
        id: 2,
        lastName: 'teacher2LastName',
        firstName: 'teacher2FirstName',
        createdAt: new Date(),
        updatedAt: new Date()
    }];

    it('should create a session (admin)', () => {
        loginAsAdmin();
        interceptSessions(sessions);
        interceptTeachers(teachers);

        cy.contains('Create').click();
        cy.contains('Create session');
        cy.get('button[type="submit"]').should('be.disabled');

        cy.get('input[formControlName="name"]').type("my yoga session");
        cy.get('input[formControlName="date"]').type("2024-03-18");
        cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('teacher1FirstName').click();
        cy.get('textarea[formControlName="description"]').type("this is a new yoga session");

        cy.get('button[type="submit"]').should('not.be.disabled');

        const newSession = {
            id: 3,
            name: "my yoga session",
            date: "2024-03-18T08:00:00.000+00:00",
            teacher_id: 1,
            description: "this is a new yoga session",
            users: [],
            createdAt: new Date(),
            updatedAt: new Date()
        };

        cy.intercept('POST', '/api/session', {
            body: newSession
        });

        cy.intercept('GET', '/api/session', {
            body: [...sessions, newSession]
        });

        cy.contains('Save').click();
        
        cy.url().should('include', 'sessions');
        cy.contains('this is a new yoga session');
    });

    it('should update a session (admin)', () => {
       
        const session1 = sessions[0];
        const teacher1 = teachers[0];

        interceptSessions(sessions);
        cy.intercept('GET', '/api/session/1', session1);
        cy.intercept('GET', '/api/teacher/1', teacher1);
        interceptTeachers(teachers);

        loginAsAdmin();
        cy.contains('Edit').click();
        cy.contains('Update session');

        cy.get('input[formControlName="name"]').clear().type("my session 1 updated");
        cy.get('input[formControlName="date"]').clear().type("2024-03-18");
        cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('teacher1FirstName').click();
        cy.get('textarea[formControlName="description"]').clear().type("session 1 updated");

        cy.get('button[type="submit"]').should('not.be.disabled');

        const session1updated = {
            id: 1,
            name: "my session 1 updated",
            date: "2024-03-18T08:00:00.000+00:00",
            teacher_id: 1,
            description: "session 1 updated",
            users: [],
            createdAt: "2024-03-15T08:23:02",
            updatedAt: new Date()
        };

        cy.intercept('PUT', '/api/session/1', {
            body: session1updated
        });

        cy.intercept('GET', '/api/session', {
            body: [session1updated, sessions[1]]
        });

        cy.contains('Save').click();
        cy.url().should('include', 'sessions');
        cy.contains('my session 1 updated');
    });
});
