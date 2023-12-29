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

describe('Acronym e2e test', () => {
  const acronymPageUrl = '/acronym';
  const acronymPageUrlPattern = new RegExp('/acronym(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const acronymSample = { termOrAcronym: 'blissfully beside' };

  let acronym;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/acronyms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/acronyms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/acronyms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (acronym) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/acronyms/${acronym.id}`,
      }).then(() => {
        acronym = undefined;
      });
    }
  });

  it('Acronyms menu should load Acronyms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('acronym');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Acronym').should('exist');
    cy.url().should('match', acronymPageUrlPattern);
  });

  describe('Acronym page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(acronymPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Acronym page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/acronym/new$'));
        cy.getEntityCreateUpdateHeading('Acronym');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', acronymPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/acronyms',
          body: acronymSample,
        }).then(({ body }) => {
          acronym = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/acronyms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/acronyms?page=0&size=20>; rel="last",<http://localhost/api/acronyms?page=0&size=20>; rel="first"',
              },
              body: [acronym],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(acronymPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Acronym page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('acronym');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', acronymPageUrlPattern);
      });

      it('edit button click should load edit Acronym page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Acronym');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', acronymPageUrlPattern);
      });

      it('edit button click should load edit Acronym page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Acronym');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', acronymPageUrlPattern);
      });

      it('last delete button click should delete instance of Acronym', () => {
        cy.intercept('GET', '/api/acronyms/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('acronym').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', acronymPageUrlPattern);

        acronym = undefined;
      });
    });
  });

  describe('new Acronym page', () => {
    beforeEach(() => {
      cy.visit(`${acronymPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Acronym');
    });

    it('should create an instance of Acronym', () => {
      cy.get(`[data-cy="termOrAcronym"]`).type('gleefully invent');
      cy.get(`[data-cy="termOrAcronym"]`).should('have.value', 'gleefully invent');

      cy.get(`[data-cy="name"]`).type('nor');
      cy.get(`[data-cy="name"]`).should('have.value', 'nor');

      cy.get(`[data-cy="definition"]`).type('sabre admired why');
      cy.get(`[data-cy="definition"]`).should('have.value', 'sabre admired why');

      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        acronym = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', acronymPageUrlPattern);
    });
  });
});
