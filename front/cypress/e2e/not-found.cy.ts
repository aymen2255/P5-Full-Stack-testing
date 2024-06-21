describe('Notfound component', () => {
    beforeEach(() => {
      cy.visit('/not-found'); // Remplacez '/not-found' par l'URL réelle de votre composant "notfound"
    });
  
    it('should display the "Page not found !" message', () => {
      cy.get('h1').should('contain', 'Page not found !');
    });
  });
  