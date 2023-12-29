import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IContext } from 'app/shared/model/context.model';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { IAcronym } from 'app/shared/model/acronym.model';
import { getEntity, updateEntity, createEntity, reset } from './acronym.reducer';

export const AcronymUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const contexts = useAppSelector(state => state.context.entities);
  const acronymEntity = useAppSelector(state => state.acronym.entity);
  const loading = useAppSelector(state => state.acronym.loading);
  const updating = useAppSelector(state => state.acronym.updating);
  const updateSuccess = useAppSelector(state => state.acronym.updateSuccess);

  const handleClose = () => {
    navigate('/acronym' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getContexts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...acronymEntity,
      ...values,
      contexts: mapIdList(values.contexts),
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...acronymEntity,
          user: acronymEntity?.user?.id,
          contexts: acronymEntity?.contexts?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="acronymDefinitionApp.acronym.home.createOrEditLabel" data-cy="AcronymCreateUpdateHeading">
            <Translate contentKey="acronymDefinitionApp.acronym.home.createOrEditLabel">Create or edit a Acronym</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="acronym-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('acronymDefinitionApp.acronym.termOrAcronym')}
                id="acronym-termOrAcronym"
                name="termOrAcronym"
                data-cy="termOrAcronym"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('acronymDefinitionApp.acronym.name')}
                id="acronym-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('acronymDefinitionApp.acronym.definition')}
                id="acronym-definition"
                name="definition"
                data-cy="definition"
                type="text"
              />
              <ValidatedField
                id="acronym-user"
                name="user"
                data-cy="user"
                label={translate('acronymDefinitionApp.acronym.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('acronymDefinitionApp.acronym.context')}
                id="acronym-context"
                data-cy="context"
                type="select"
                multiple
                name="contexts"
              >
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/acronym" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AcronymUpdate;
