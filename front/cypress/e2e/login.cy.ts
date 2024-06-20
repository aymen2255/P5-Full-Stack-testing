describe('Login spec', () => {
  beforeEach(() => {
    // Visiter la page de connexion avant chaque test
    cy.visit('/login');
  });

  it('Login successfull', () => {

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
  })

  it('should display an error message when login fails', () => {
    // Remplir le formulaire avec des informations d'identification incorrectes
    cy.get('#mat-input-0').type('wrong.email@example.com');
    cy.get('#mat-input-1').type('wrong-password');

    // Soumettre le formulaire
    cy.get('.mat-raised-button').click();

    // Vérifier si le message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred');
  });
});