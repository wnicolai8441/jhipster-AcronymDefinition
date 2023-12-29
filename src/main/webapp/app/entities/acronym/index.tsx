import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Acronym from './acronym';
import AcronymDetail from './acronym-detail';
import AcronymUpdate from './acronym-update';
import AcronymDeleteDialog from './acronym-delete-dialog';

const AcronymRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Acronym />} />
    <Route path="new" element={<AcronymUpdate />} />
    <Route path=":id">
      <Route index element={<AcronymDetail />} />
      <Route path="edit" element={<AcronymUpdate />} />
      <Route path="delete" element={<AcronymDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AcronymRoutes;
