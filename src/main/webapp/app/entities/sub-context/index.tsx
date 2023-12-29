import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubContext from './sub-context';
import SubContextDetail from './sub-context-detail';
import SubContextUpdate from './sub-context-update';
import SubContextDeleteDialog from './sub-context-delete-dialog';

const SubContextRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubContext />} />
    <Route path="new" element={<SubContextUpdate />} />
    <Route path=":id">
      <Route index element={<SubContextDetail />} />
      <Route path="edit" element={<SubContextUpdate />} />
      <Route path="delete" element={<SubContextDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubContextRoutes;
