import axios from "axios";
import bigInt from "big-integer";
import CryptoJS from "crypto-js";
import {ROOT_URL} from "@/properties";
import {encrypt} from "@/services/encDec";
import router from "@/router.js";


function getSHA256(value) {
    return CryptoJS.SHA256(value.toString());
}


export default class Auth {
    constructor() {
        this.a = bigInt.randBetween(10 ** 11, 10 ** 12);
        this.p = null;
        this.g = null;
        this.K = null;
    }

    async getSessionId() {
        try {
            const response = await axios.post(
                `${ROOT_URL}/auth/get_dh`, {
                    sessionId: localStorage.getItem('sessionId') === null ?
                        'null' :
                        localStorage.getItem('sessionId'),
                }
            );
            this.p = bigInt(response.data.p);
            this.g = bigInt(response.data.g);
            const session_id = response.data.sessionId;

            localStorage.setItem('sessionId', session_id);
            await this.getB();
        } catch (error) {
            console.log(error);
        }
    }

    async getB() {
        try {
            let A_val = this.g.modPow(this.a, this.p).toString();
            const response = await axios.post(`${ROOT_URL}/auth/get_dh_key`, {
                A: A_val,
                sessionId: localStorage.getItem('sessionId')
            });
            this.K = bigInt(response.data.b).modPow(this.a, this.p);
            this.K = getSHA256(this.K);
        } catch (error) {
            console.log(error);
        }
    }

    async accessCheck(current_path) {
        try {
            await axios.post(`${ROOT_URL}/auth/verify`, {
                token: encrypt(localStorage.getItem('token'), this.K),
                sessionId: localStorage.getItem('sessionId') === null ? 'null' : localStorage.getItem('sessionId'),
            });
            if (current_path === '/register' || current_path === '/login') {
                await router.push('/');
            }

        } catch (error) {
            if (error.response && error.response.status === 403) {
                if (!(current_path === '/register' || current_path === '/login')) {
                    await router.push('/login');
                }
            }
        }
    }
}