import { IAcronym } from 'app/shared/model/acronym.model';
import { ISubContext } from 'app/shared/model/sub-context.model';

export interface IContext {
  id?: number;
  name?: string;
  acronyms?: IAcronym[] | null;
  subContexts?: ISubContext[] | null;
}

export const defaultValue: Readonly<IContext> = {};
