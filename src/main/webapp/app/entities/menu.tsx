import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/acronym">
        <Translate contentKey="global.menu.entities.acronym" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/context">
        <Translate contentKey="global.menu.entities.context" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sub-context">
        <Translate contentKey="global.menu.entities.subContext" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
