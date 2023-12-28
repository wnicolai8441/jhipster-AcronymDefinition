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

describe('SubContext e2e test', () => {
  const subContextPageUrl = '/sub-context';
  const subContextPageUrlPattern = new RegExp('/sub-context(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subContextSample = { name: 'forenenst recapture sensitivity' };

  let subContext;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sub-contexts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sub-contexts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sub-contexts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subContext) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-contexts/${subContext.id}`,
      }).then(() => {
        subContext = undefined;
      });
    }
  });

  it('SubContexts menu should load SubContexts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sub-context');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubContext').should('exist');
    cy.url().should('match', subContextPageUrlPattern);
  });

  describe('SubContext page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subContextPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubContext page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sub-context/new$'));
        cy.getEntityCreateUpdateHeading('SubContext');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subContextPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sub-contexts',
          body: subContextSample,
        }).then(({ body }) => {
          subContext = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sub-contexts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sub-contexts?page=0&size=20>; rel="last",<http://localhost/api/sub-contexts?page=0&size=20>; rel="first"',
              },
              body: [subContext],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subContextPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubContext page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subContext');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subContextPageUrlPattern);
      });

      it('edit button click should load edit SubContext page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubContext');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subContextPageUrlPattern);
      });

      it('edit button click should load edit SubContext page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubContext');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subContextPageUrlPattern);
      });

      it('last delete button click should delete instance of SubContext', () => {
        cy.intercept('GET', '/api/sub-contexts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subContext').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subContextPageUrlPattern);

        subContext = undefined;
      });
    });
  });

  describe('new SubContext page', () => {
    beforeEach(() => {
      cy.visit(`${subContextPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubContext');
    });

    it('should create an instance of SubContext', () => {
      cy.get(`[data-cy="name"]`).type('while yet vice');
      cy.get(`[data-cy="name"]`).should('have.value', 'while yet vice');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subContext = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subContextPageUrlPattern);
    });
  });
});
