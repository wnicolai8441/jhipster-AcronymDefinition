import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './acronym.reducer';

export const AcronymDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const acronymEntity = useAppSelector(state => state.acronym.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="acronymDetailsHeading">
          <Translate contentKey="acronymDefinitionApp.acronym.detail.title">Acronym</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{acronymEntity.id}</dd>
          <dt>
            <span id="termOrAcronym">
              <Translate contentKey="acronymDefinitionApp.acronym.termOrAcronym">Term Or Acronym</Translate>
            </span>
          </dt>
          <dd>{acronymEntity.termOrAcronym}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="acronymDefinitionApp.acronym.name">Name</Translate>
            </span>
          </dt>
          <dd>{acronymEntity.name}</dd>
          <dt>
            <span id="definition">
              <Translate contentKey="acronymDefinitionApp.acronym.definition">Definition</Translate>
            </span>
          </dt>
          <dd>{acronymEntity.definition}</dd>
          <dt>
            <Translate contentKey="acronymDefinitionApp.acronym.user">User</Translate>
          </dt>
          <dd>{acronymEntity.user ? acronymEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="acronymDefinitionApp.acronym.context">Context</Translate>
          </dt>
          <dd>
            {acronymEntity.contexts
              ? acronymEntity.contexts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {acronymEntity.contexts && i === acronymEntity.contexts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/acronym" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/acronym/${acronymEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AcronymDetail;
