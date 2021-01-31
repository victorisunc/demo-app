import {doDelete, doGetJson, doPostJson, doPutJson} from "./request";

export function getAds() {
    return doGetJson(`/ads`);
}

export function getAd(id) {
    return doGetJson(`/ads/${id}`);
}

export function createAd(ad) {
    return doPostJson(`/ads`, ad);
}

export function updateAd(id, ad) {
    return doPutJson(`/ads/${id}`, ad);
}

export function deleteAd(id) {
    return doDelete(`/ads/${id}`);
}

