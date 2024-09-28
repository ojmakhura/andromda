import { Page } from "@app/model/page.model";
import { SearchObject } from "@app/model/search-object";

export type AppState<T, L> = {
    data: T;
    dataList: L[];
    dataPage: Page<L>;
    searchCriteria: SearchObject<T>;
    error: any;
    loading: boolean;
    success: boolean;
    messages: string[];
};
