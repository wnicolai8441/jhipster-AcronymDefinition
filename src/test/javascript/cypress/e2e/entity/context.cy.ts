import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Context e2e test', () => {
  const contextPageUrl = '/context';
  const contextPageUrlPattern = new RegExp('/context(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contextSample = { name: 'pole sail while' };

  let context;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contexts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contexts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contexts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (context) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contexts/${context.id}`,
      }).then(() => {
        context = undefined;
      });
    }
  });

  it('Contexts menu should load Contexts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('context');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Context').should('exist');
    cy.url().should('match', contextPageUrlPattern);
  });

  describe('Context page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contextPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Context page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/context/new$'));
        cy.getEntityCreateUpdateHeading('Context');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contextPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contexts',
          body: contextSample,
        }).then(({ body }) => {
          context = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contexts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contexts?page=0&size=20>; rel="last",<http://localhost/api/contexts?page=0&size=20>; rel="first"',
              },
              body: [context],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contextPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Context page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('context');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contextPageUrlPattern);
      });

      it('edit button click should load edit Context page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Context');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contextPageUrlPattern);
      });

      it('edit button click should load edit Context page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Context');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contextPageUrlPattern);
      });

      it('last delete button click should delete instance of Context', () => {
        cy.intercept('GET', '/api/contexts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('context').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contextPageUrlPattern);

        context = undefined;
      });
    });
  });

  describe('new Context page', () => {
    beforeEach(() => {
      cy.visit(`${contextPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Context');
    });

    it('should create an instance of Context', () => {
      cy.get(`[data-cy="name"]`).type('evolution finally estuary');
      cy.get(`[data-cy="name"]`).should('have.value', 'evolution finally estuary');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        context = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', contextPageUrlPattern);
    });
  });
});
