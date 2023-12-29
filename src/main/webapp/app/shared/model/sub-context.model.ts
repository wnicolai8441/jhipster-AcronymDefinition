import { IAcronym } from 'app/shared/model/acronym.model';

export interface ISubContext {
  id?: number;
  name?: string;
  acronyms?: IAcronym[] | null;
}

export const defaultValue: Readonly<ISubContext> = {};
