import { IUser } from 'app/shared/model/user.model';
import { IContext } from 'app/shared/model/context.model';

export interface IAcronym {
  id?: number;
  termOrAcronym?: string;
  name?: string | null;
  definition?: string | null;
  user?: IUser | null;
  contexts?: IContext[] | null;
}

export const defaultValue: Readonly<IAcronym> = {};
