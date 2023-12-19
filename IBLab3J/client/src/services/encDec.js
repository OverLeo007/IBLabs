import CryptoJS from 'crypto-js'


export function decrypt(encryptedMessage, key) {
    return CryptoJS.AES.decrypt(encryptedMessage, key, {mode: CryptoJS.mode.ECB}).toString(CryptoJS.enc.Utf8);
}

export function encrypt(message, key) {
    const enc = CryptoJS.AES.encrypt(message, key, { mode: CryptoJS.mode.ECB });
    return enc.toString();
}


export function decryptJsonList(data, dec_key) {
    return data.map(obj => decryptJson(obj, dec_key));
}

export function decryptJson(data, dec_key) {
    return Object.fromEntries(
        Object.entries(data).map(([key, value]) => [key, decrypt(value, dec_key)])
    );
}