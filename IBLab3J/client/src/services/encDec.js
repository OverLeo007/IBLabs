import CryptoJS from 'crypto-js'


export function decrypt(encryptedMessage, key) {
    return CryptoJS.AES.decrypt(encryptedMessage, key, {mode: CryptoJS.mode.ECB}).toString(CryptoJS.enc.Utf8);
}

export function encrypt(message, key) {
    const enc = CryptoJS.AES.encrypt(message, key, { mode: CryptoJS.mode.ECB });
    return enc.toString();
}


export function decryptData(data, key) {
    const res = [];

    for (const obj of data) {
        res.push(decryptJson(obj, key));
    }

    return res;
}

export function decryptJson(data, key) {
    const decryptedData = {};

    for (const [key, value] of Object.entries(data)) {
        decryptedData[key] = decrypt(value, key);
    }
    return decryptedData
}