describe('list component', () => {
    beforeEach(() => {
       

        cy.intercept('GET', '/api/session', {
            body: [
                {
                    id: 1,
                    name: 'Session 1',
                    date: '2024-06-22T10:00:00',
                    description: 'Description of Session 1',
                    createdAt: '2024-06-22T10:00:00',
                    updatedAt: '2024-06-22T10:00:00',
                    users: [],
                },
                {
                    id: 2,
                    name: 'Session 2',
                    date: '2023-03-17T14:00:00',
                    description: 'Description of Session 2',
                    createdAt: '2024-06-22T10:00:00',
                    updatedAt: '2024-06-22T10:00:00',
                    users: [],
                },
            ],
        });

       
    });

    it('should display a list of sessions for simple user', () => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: false
            },
        });

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        cy.url().should('include', '/sessions');

        cy.url().should('include', '/sessions');

        cy.get('.item').should('have.length', 2);
        cy.get('.items > :nth-child(1) > .mat-card-header > .mat-card-header-text > .mat-card-title').should('contain', 'Session 1');
        cy.get(':nth-child(2) > .mat-card-header > .mat-card-header-text > .mat-card-title').should('contain', 'Session 2');
        cy.get(':nth-child(1) > .mat-card-actions > .mat-focus-indicator').should('be.visible');
        cy.get(':nth-child(2) > .mat-card-actions > .mat-focus-indicator').should('be.visible');
    });


    it('should display a list of sessions for admin user', () => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        });

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        cy.url().should('include', '/sessions');

        cy.url().should('include', '/sessions');

        cy.get('.item').should('have.length', 2);
        cy.get('.items > :nth-child(1) > .mat-card-header > .mat-card-header-text > .mat-card-title').should('contain', 'Session 1');
        cy.get(':nth-child(2) > .mat-card-header > .mat-card-header-text > .mat-card-title').should('contain', 'Session 2');
        cy.get(':nth-child(1) > .mat-card-actions > .mat-focus-indicator').should('be.visible');
        cy.get(':nth-child(2) > .mat-card-actions > .mat-focus-indicator').should('be.visible');

        cy.get('[fxlayout="row"] > .mat-focus-indicator').should('be.visible');
        cy.get(':nth-child(2) > .mat-card-actions > .ng-star-inserted').should('be.visible');
    });
    
});
