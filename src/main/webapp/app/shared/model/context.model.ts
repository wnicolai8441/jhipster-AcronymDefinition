import { ISubContext } from 'app/shared/model/sub-context.model';
import { IAcronym } from 'app/shared/model/acronym.model';

export interface IContext {
  id?: number;
  name?: string;
  subContexts?: ISubContext[] | null;
  acronyms?: IAcronym[] | null;
}

export const defaultValue: Readonly<IContext> = {};
