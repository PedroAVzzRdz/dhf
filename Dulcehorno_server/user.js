const dotenv = require('dotenv');
dotenv.config();
const mongoose = require('mongoose');


mongoose.set('strictQuery', false)


// Items dentro de un receipt
const ItemSchema = new mongoose.Schema({
  product: { 
    type: mongoose.Schema.Types.ObjectId, 
    ref: 'Product', // referencia a la colección de productos
    required: true
  },
  quantity: {
    type: Number,
    required: true
  }
}, { _id: false });

// Receipts
const ReceiptSchema = new mongoose.Schema({
  id: { type: String, required: true },
  requestDate: { type: String, required: true },
  estimatedArrivalDate: { type: String, required: true },
  deliveryLocation: { type: String, required: true },
  items: [ItemSchema],
  total: { type: Number, required: true }
}, { _id: false });

// Usuario
const userSchema = new mongoose.Schema({
  username: { type: String, required: true },
  password: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  receipts: [ReceiptSchema]
}, { timestamps: true });


userSchema.set('toJSON', {
  transform: (document, returnedObject) => {
    returnedObject.id = returnedObject._id.toString();
    delete returnedObject._id;
    delete returnedObject.__v;
  }
});

module.exports = mongoose.model('User', userSchema);