import { IContext } from 'app/shared/model/context.model';

export interface ISubContext {
  id?: number;
  name?: string;
  context?: IContext | null;
}

export const defaultValue: Readonly<ISubContext> = {};
