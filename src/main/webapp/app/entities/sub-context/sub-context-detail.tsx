import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-context.reducer';

export const SubContextDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subContextEntity = useAppSelector(state => state.subContext.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subContextDetailsHeading">
          <Translate contentKey="acronymDefinitionApp.subContext.detail.title">SubContext</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subContextEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="acronymDefinitionApp.subContext.name">Name</Translate>
            </span>
          </dt>
          <dd>{subContextEntity.name}</dd>
          <dt>
            <Translate contentKey="acronymDefinitionApp.subContext.context">Context</Translate>
          </dt>
          <dd>{subContextEntity.context ? subContextEntity.context.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/sub-context" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-context/${subContextEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubContextDetail;
