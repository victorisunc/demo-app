export const DEV = "dev";
export const STAGING = "staging";
export const PRODUCTION = "production";

const configs = {
    [DEV]: {
        API_URL: "http://localhost:8080",
    },
    [STAGING]: {
        API_URL: "https://api.demoapp.staging",
    },
    [PRODUCTION]: {
        API_URL: "https://api.demoapp.com",
    }
};

export function getProfile() {
    const hostname = window.location.hostname;
    if (hostname.indexOf('demoapp.staging') >= 0) {
        return STAGING;
    } else if (hostname.indexOf('demoapp.com') >= 0) {
        return PRODUCTION;
    } else {
        return DEV;
    }
}

export function getConfig() {
    return configs[getProfile()];
}

export const config = getConfig();

