import { Page } from "@models/page.model";
import { SearchObject } from "@models/search-object";

export type AppState<T, L> = {
    data: T;
    dataList: L[];
    dataPage: Page<L>;
    searchCriteria: SearchObject<T>;
    error: any;
    loading: boolean;
    success: boolean;
    messages: string[];
    loaderMessage: string;
};
