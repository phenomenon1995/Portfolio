import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API
});


export default {
  getProducts() {
    let token = localStorage.getItem('token')
    return http.post('/menu', 
      {
        categories: []
      },
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }
    );
  },

  getAllToppings() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Regular Topping", "Premium Topping"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  getAllDrinks() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Drink"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  getAllDesserts() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Dessert"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  getAllAppetizers() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Appetizer"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  getAllSalads() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Salad"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  getAllSpecialtyPizzas() {
    let token = localStorage.getItem('token')
    return http.post('/menu', {
      categories: ["Specialty Pizza"]
    },
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  updateProduct(product) {
    let token = localStorage.getItem('token')
    console.log(product)
    return http.put(`/menu/${product.product_id}`, 
      product
    ,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  createProduct(product) {
    let token = localStorage.getItem('token')
    return http.post(`/menu/add`, 
      product
    ,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },
  getProductById(id) {
    let token = localStorage.getItem('token')
    console.log(id)
    return http.get(`/menu/${id}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  }
}