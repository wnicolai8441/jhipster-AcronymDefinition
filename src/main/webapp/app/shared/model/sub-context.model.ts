import { IContext } from 'app/shared/model/context.model';

export interface ISubContext {
  id?: number;
  name?: string;
  contexts?: IContext[] | null;
}

export const defaultValue: Readonly<ISubContext> = {};
