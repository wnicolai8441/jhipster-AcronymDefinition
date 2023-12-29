import { IAcronym } from 'app/shared/model/acronym.model';

export interface IContext {
  id?: number;
  name?: string;
  acronyms?: IAcronym[] | null;
}

export const defaultValue: Readonly<IContext> = {};
