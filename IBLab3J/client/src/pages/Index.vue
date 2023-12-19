<script>
import Header from "@/components/Header.vue";
import axios from "axios";
import {decryptJson, decryptJsonList, encrypt} from "@/services/encDec.js";
import {ROOT_URL} from "@/properties.js";
import router from "@/router.js";

export default {

  name: "Index",

  inject: ['auth'],

  components: {Header},

  data() {
    return {
      instruments: [],
      isModalOpen: false,
      instrumentModal: this.getClearModal(),
      errors: {
        name: null,
        type: null,
        brand: null,
        price: null,
        quantity: null,
      }
    }
  },

  methods: {
    async getInstruments() {
      try {
        const response = await axios.post(`${ROOT_URL}/instrument`, {
          token: encrypt(localStorage.getItem('token'), this.auth.K),
          sessionId: localStorage.getItem('sessionId'),
        });
        this.instruments = decryptJsonList(response.data.instrumentsList, this.auth.K);
      } catch (error) {
        console.log(error)
      }
    },

    async deleteInstrument(instrumentId) {
      try {
        await axios.post(`${ROOT_URL}/instrument/del`, {
          token: encrypt(localStorage.getItem('token'), this.auth.K),
          sessionId: localStorage.getItem('sessionId'),
          id: encrypt(instrumentId, this.auth.K),
        });
        this.instruments = this.instruments.filter(
            (instrument) => {
              return instrument.id !== instrumentId
            }
        )
      } catch (error) {
        console.log(error);
      }
    },

    async validateForm() {
      this.flushErrors()
      if (!this.instrumentModal.name || this.instrumentModal.name.length < 3 || this.instrumentModal.name.length > 30) {
        this.errors.name = 'Len of name is not between 3 and 30.';
      }

      if (!this.instrumentModal.type || this.instrumentModal.type.length < 3 || this.instrumentModal.type.length > 30) {
        this.errors.type = 'Len of type is not between 3 and 30.';
      }

      if (!this.instrumentModal.brand || this.instrumentModal.brand.length < 3 || this.instrumentModal.brand.length > 30) {
        this.errors.brand = 'Len of brand is not between 3 and 30.';
      }

      if (!this.instrumentModal.price || isNaN(parseFloat(this.instrumentModal.price))) {
        this.errors.price = 'Price must be a valid number.';
      }

      if (!this.instrumentModal.quantity || this.instrumentModal.quantity <= 0 || this.instrumentModal.quantity > 9999) {
        this.errors.quantity = 'Quantity must be a positive number and lower then 9999.';
      }

      if (Object.values(this.errors).some(error => error !== null)) {
        return;
      }
      await this.sendData();
    },

    getClearModal() {
      return {
        isEdit: false,
        title: '',
        name: '',
        type: '',
        brand: '',
        price: '',
        quantity: '',
        id: ''
      }
    },

    getClearErrors() {
      return {
        name: null,
        type: null,
        brand: null,
        price: null,
        quantity: null,
      }
    },

    flushModal() {
      console.log("modal cleared")
      this.instrumentModal = this.getClearModal()
    },

    flushErrors() {
      this.errors = this.getClearErrors()
    },

    async editInstrument(instrumentId) {
      const instrument = this.instruments.find((instrument) => instrument.id === instrumentId);
      this.instrumentModal = Object.assign({title: `Edit ${instrument.name}`}, instrument)
      this.instrumentModal.isEdit = true
      this.openModal()
    },

    async newInstrument() {
      this.instrumentModal.title = "New instrument"
      this.instrumentModal.isEdit = false
      this.openModal()
    },

    openModal() {
      this.flushErrors()
      this.isModalOpen = true;
    },

    closeModal() {
      this.isModalOpen = false;
      this.flushModal()
    },

    async sendData() {
      if (this.instrumentModal.isEdit) {
        await this.sendEditRequest()
      } else {
        await this.sendAddRequest()
      }
      this.closeModal()
    },

    async sendEditRequest() {
      try {
        const respData = {
          token: encrypt(localStorage.getItem('token'), this.auth.K),
          sessionId: localStorage.getItem('sessionId'),
          id: encrypt(this.instrumentModal.id.toString(), this.auth.K),
          name: encrypt(this.instrumentModal.name, this.auth.K),
          type: encrypt(this.instrumentModal.type, this.auth.K),
          brand: encrypt(this.instrumentModal.brand, this.auth.K),
          price: encrypt(this.instrumentModal.price.toString(), this.auth.K),
          quantity: encrypt(this.instrumentModal.quantity.toString(), this.auth.K),
        }
        await axios.post(`${ROOT_URL}/instrument/edit`, respData)
        const instrument = this.instruments.find((instrument) => instrument.id === this.instrumentModal.id);
        const instrumentModalCopy = {...this.instrumentModal};
        Object.keys(instrumentModalCopy).forEach(key => {
          if (key in instrument) {
            instrument[key] = instrumentModalCopy[key];
          }
        });
      } catch (error) {
      }
    },

    async sendAddRequest() {
      const respData = {
        token: encrypt(localStorage.getItem('token'), this.auth.K),
        sessionId: localStorage.getItem('sessionId'),
        name: encrypt(this.instrumentModal.name, this.auth.K),
        type: encrypt(this.instrumentModal.type, this.auth.K),
        brand: encrypt(this.instrumentModal.brand, this.auth.K),
        price: encrypt(this.instrumentModal.price.toString(), this.auth.K),
        quantity: encrypt(this.instrumentModal.quantity.toString(), this.auth.K),
      }
      try {
        const response = await axios.post(`${ROOT_URL}/instrument/new`, respData)
        this.instruments.push(decryptJson(response.data, this.auth.K))
      } catch (error) {

      }
    }

  },
  async mounted() {
    await this.auth.getSessionId();
    await this.auth.accessCheck(this.$route.path);
    await this.getInstruments();
  }
}
</script>

<template>
  <Header></Header>
  <div class="container">
    <div class="row">
      <button type="button" @click="newInstrument()" class="col s12 btn btn-primary">Add new instrument</button>
    </div>
    <div class="row">
      <div v-for="instrument in instruments" :key="instrument.id" class="col s12 m6">
        <div class="card blue-grey darken-1">
          <div class="card-content white-text">
            <span>{{ instrument.name + ' by ' + instrument.brand }}</span>
            <span>{{ instrument.type }}</span>
            <span>{{ instrument.price + ' $' }}</span>
            <span>{{ 'We have ' + instrument.quantity + ' copies' }}</span>
          </div>
          <div class="card-action">
            <a @click="editInstrument(instrument.id)" class="waves-effect waves-light btn blue">edit</a>
            <a @click="deleteInstrument(instrument.id)" class="waves-effect waves-light btn red">delete</a>
          </div>
        </div>
      </div>
    </div>


    <div v-show="isModalOpen" class="modal-overlay-my" @click="closeModal">
      <div class="modal-my card blue-grey darken-1 container" @click.stop>
        <div class="row">
          <div class="col s12">
            <h4 v-text="instrumentModal.title"></h4>
          </div>
        </div>

        <form>
          <div class="row">
            <div class="input-field col s12">
              <label for="name">Instrument Name:</label>
              <input type="text" id="name" name="name" class="white-text" v-model="instrumentModal.name" required>
            </div>
            <div v-show="errors.name" class="new badge red col">
              {{ errors.name }}
            </div>
          </div>

          <div class="row">
            <div class="input-field col s12">
              <label for="type">Instrument Type:</label>
              <input type="text" id="type" name="type" class="white-text" v-model="instrumentModal.type" required>
            </div>
            <div v-show="errors.type" class="new badge red col">
              {{ errors.type }}
            </div>
          </div>

          <div class="row">
            <div class="input-field col s12">
              <label for="brand">Brand:</label>
              <input type="text" id="brand" name="brand" class="white-text" v-model="instrumentModal.brand" required>
            </div>
            <div v-show="errors.brand" class="new badge red col">
              {{ errors.brand }}
            </div>
          </div>

          <div class="row">
            <div class="input-field col s12">
              <label for="price">Price:</label>
              <input type="text" inputmode="decimal" id="price" class="white-text"
                     name="price" pattern="[0-9]*[.,]?[0-9]*" v-model="instrumentModal.price"
                     required>
            </div>
            <div v-show="errors.price" class="new badge red col">
              {{ errors.price }}
            </div>
          </div>

          <div class="row">
            <div class="input-field col s12">
              <label for="quantity">Quantity:</label>
              <input type="number" id="quantity" name="quantity" class="white-text" v-model="instrumentModal.quantity"
                     required>
            </div>
            <div v-show="errors.quantity" class="new badge red col">
              {{ errors.quantity }}
            </div>
          </div>

          <div class="row">
            <div class="col s12">
              <button @click="validateForm" class="btn btn-modal waves-effect waves-light" type="button" name="action">
                {{ instrumentModal.isEdit ? 'Edit this!' : 'Add this!' }}
                <i class="material-icons right">send</i>
              </button>
            </div>
          </div>

        </form>
      </div>
    </div>
  </div>


</template>

<style scoped>

h4 {
  margin-top: 0;
  margin-bottom: 50px;
  text-align: center;
}

.card-content {
  height: 150px;
  overflow: hidden;
}

.card-content span {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.btn {
  width: 100%;
  margin-top: 20px;
}

.btn-modal {
  width: 20%;

}

.modal-overlay-my {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-my {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 70%;
  max-width: 90%;
}

</style>