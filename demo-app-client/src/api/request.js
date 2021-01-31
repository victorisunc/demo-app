import * as axios from "axios";
import {stringify} from 'query-string';
import {config} from "./config";


export const httpClient = axios.create({
    baseURL: config.API_URL,
    timeout: 60000
});


export function doRequest(config) {
    return httpClient.request(config);
}

export function doGetJson(path, params) {
    if (params) {
        return doRequest({ url: `${path}?${stringify(params, { skipNull: true, strict: false })}`, method: 'get' });
    }
    return doRequest({ url: path, method: 'get' });
}

export function doPutJson(path, obj) {
    return doRequest({
        url: path,
        method: 'put',
        headers: { 'Content-Type': 'application/json' },
        data: JSON.stringify(obj)
    });
}

export function doPatchJson(path, obj) {
    return doRequest({
        url: path,
        method: 'patch',
        headers: { 'Content-Type': 'application/json' },
        data: JSON.stringify(obj)
    });
}

export function doPostJson(path, obj) {
    return doRequest({
        url: path,
        method: 'post',
        headers: { 'Content-Type': 'application/json' },
        data: JSON.stringify(obj)
    });
}

export function doDelete(path) {
    return doRequest({
        url: path,
        method: 'delete',
        headers: { 'Content-Type': 'application/json' },
    });
}


