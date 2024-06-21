describe('Register form', () => {
    beforeEach(() => {
      // Visiter la page d'inscription avant chaque test
      cy.visit('/register');
    });
  
    it('should display an error message when registration fails', () => {
      // Remplir le formulaire avec des informations d'inscription incorrectes
      cy.get('input[formControlName="firstName"]').type('John');
      cy.get('input[formControlName="lastName"]').type('Doe');
      cy.get('input[formControlName="email"]').type('invalid.email@example.com');
      cy.get('input[formControlName="password"]').type('invalid-password');
  
      // Stub la réponse de l'API pour simuler une inscription échouée
      cy.intercept('POST', '/api/register', {
        statusCode: 400, // Code d'état HTTP pour "Bad Request"
        body: {
          error: 'Email is already taken',
        },
      });
  
      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();
  
      // Vérifier si le message d'erreur est affiché
      cy.get('.error').should('contain', 'An error occurred');
    });
  
    it('should navigate to login page when registration succeeds', () => {
      // Remplir le formulaire avec des informations d'inscription correctes
      cy.get('input[formControlName="firstName"]').type('John');
      cy.get('input[formControlName="lastName"]').type('Doe');
      cy.get('input[formControlName="email"]').type('valid.email@example.com');
      cy.get('input[formControlName="password"]').type('valid-password');
  
      // Stub la réponse de l'API pour simuler une inscription réussie
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 201, // Code d'état HTTP pour "Created"
        body: {}, // Réponse vide pour une inscription réussie
      }).as('register');
  
      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();
  
      // Vérifier si la requête API a été appelée avec les bonnes données
      cy.wait('@register').its('request.body').should('deep.equal', {
        firstName: 'John',
        lastName: 'Doe',
        email: 'valid.email@example.com',
        password: 'valid-password',
      });
  
      // Vérifier si la redirection vers la page de connexion a eu lieu
      cy.url().should('include', '/login');
    });
  });
  