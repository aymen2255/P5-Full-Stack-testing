describe('User component', () => {
    beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []).as('session')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')
    });

    it('should display simple user data', () => {
        cy.intercept('GET', '/api/user/1', {
            body: {
                id: 1,
                email: 'toto@toto.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: false,
                createdAt: '2023-03-14',
                updatedAt: '2023-03-14',
            },
        })
        cy.get('[routerlink="me"]').click();

        cy.url().should('include', '/me')
        // Vérifier que les données de l'utilisateur sont affichées correctement
        cy.get('mat-card-title h1').should('contain', 'User information');

        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(1)').should('contain', 'Name: firstName LASTNAME');
        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(2)').should('contain', 'Email: toto@toto.com');
        cy.get('.my2 > p').should('contain', 'Delete my account');
        cy.get('.ml1').should('be.visible');
        cy.get('.p2 > :nth-child(1)').should('contain', 'March 14, 2023');
        cy.get('.p2 > :nth-child(2)').should('contain', 'March 14, 2023');
    });

    it('should display admin user data', () => {
        cy.intercept('GET', '/api/user/1', {
            body: {
                id: 1,
                email: 'toto@toto.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: true,
                createdAt: '2023-03-14',
                updatedAt: '2023-03-14',
            },
        })
        cy.get('[routerlink="me"]').click();

        cy.url().should('include', '/me')
        // Vérifier que les données de l'utilisateur sont affichées correctement
        cy.get('mat-card-title h1').should('contain', 'User information');

        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(1)').should('contain', 'Name: firstName LASTNAME');
        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(2)').should('contain', 'Email: toto@toto.com');
        cy.get('.my2').should('contain', 'You are admin');
        cy.get('.p2 > :nth-child(1)').should('contain', 'March 14, 2023');
        cy.get('.p2 > :nth-child(2)').should('contain', 'March 14, 2023');
    });

    it('should navigate back', () => {
        cy.intercept('GET', '/api/user/1', {
            body: {
                id: 1,
                email: 'toto@toto.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: true,
                createdAt: '2023-03-14',
                updatedAt: '2023-03-14',
            },
        })
        cy.get('[routerlink="me"]').click();

        cy.url().should('include', '/me')
        // Vérifier que les données de l'utilisateur sont affichées correctement
        cy.get('mat-card-title h1').should('contain', 'User information');

        // Simuler un clic sur le bouton qui appelle la méthode back()
        cy.get('button[mat-icon-button]').click();

        // Vérifier que l'URL de la page a changé pour revenir à la page précédente
        cy.url().should('not.include', '/me');
    });


    it('should delete user account', () => {
        cy.intercept('GET', '/api/user/1', {
            body: {
                id: 1,
                email: 'toto@toto.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: false,
                createdAt: '2023-03-14',
                updatedAt: '2023-03-14',
            },
        })
        cy.get('[routerlink="me"]').click();

        cy.url().should('include', '/me')
        // Intercepter la requête DELETE envoyée lors de la suppression d'un utilisateur
        cy.intercept('DELETE', '/api/user/*', {
            statusCode: 204,
            body: {},
        }).as('deleteUser');

        // Cliquer sur le bouton "Delete my account"
        cy.get('button[color="warn"]').click();

        // Vérifier que la requête DELETE a été envoyée avec les bons paramètres
        cy.wait('@deleteUser').its('request.url').should('match', /\/api\/user\/\d+$/);

        // Vérifier que le message de confirmation s'affiche
        cy.get('snack-bar-container').should('contain', 'Your account has been deleted !');

        // Vérifier que la redirection vers la page d'accueil a bien eu lieu
        cy.url().should('include', '/');
    });


});
