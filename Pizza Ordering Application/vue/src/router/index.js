import { createRouter as createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

// Import components
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import Pizza from '../views/Pizza.vue';
import CustomPizza from '../views/CustomPizza.vue';
import Drink from '../views/Drink.vue';
import MyOrders from '../views/MyOrders.vue';
import Desserts from '../views/Desserts.vue';
import Sides from '../views/Sides.vue';
import Salads from '../views/Salads.vue'
import Admin from '../views/Admin.vue'

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/logout",
    name: "logout",
    component: LogoutView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: {
      requiresAuth: false
    }
  },

  // PRODUCT PAGES

    // /pizzas
  {
    path: "/pizza",
    name: "pizza",
    component: Pizza,
    meta: {
      requiresAuth: false
    }
  },

    // /customPizza

    {
      path: "/pizza/custom",
      name: "custom",
      component: CustomPizza,
      meta: {
        requiresAuth: false
      }
    },

    // /drinks

    {
      path: "/drink",
      name: "drink",
      component: Drink,
      meta: {
        requiresAuth: false
      }
    },

    // desserts

    {
      path: "/dessert",
      name: "dessert",
      component: Desserts,
      meta: {
        requiresAuth: false
      }
    },

    // sides

    {
      path: "/sides",
      name: "sides",
      component: Sides,
      meta: {
        requiresAuth: false
      }
    },

    // salads

    {
      path: "/salad",
      name: "salad",
      component: Salads,
      meta: {
        requiresAuth: false
      }
    },


  // Customer orders

    // /myOrders
    {
      path: "/myOrders",
      name: "myOrders",
      component: MyOrders,
      meta: {
        requiresAuth: true
      }
    },

  // ADMIN DASHBOARD
    // create /admin path - general overview
    // /myOrders
    // TODO: Admins only requirement must be added
    {
      path: "/admin",
      name: "admin",
      component: Admin,
      meta: {
        requiresAuth: true
      }
    },
    // create admin/viewOrders path - admins can update or cancel orders
    // create admin/viewInventory path - admins can modify inventory/stock
    // create admin/viewSpecialtyPizzas path - admins can view, delete, or add pizzas
    //
];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return {name: "login"};
  }
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;
