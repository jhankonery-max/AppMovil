import React, { useState, useEffect } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

// Importar pantallas
import LoginScreen from './src/screens/LoginScreen';
import RegistroScreen from './src/screens/RegistroScreen';
import ClienteHomeScreen from './src/screens/ClienteHomeScreen';
import RepartidorHomeScreen from './src/screens/RepartidorHomeScreen';
import PerfilScreen from './src/screens/PerfilScreen';
import CrearPedidoScreen from './src/screens/CrearPedidoScreen';
import DetallePedidoScreen from './src/screens/DetallePedidoScreen';

const Stack = createNativeStackNavigator();

const App = () => {
  const [userType, setUserType] = useState(null);

  useEffect(() => {
    setUserType('CLIENTE');
  }, []);

  const getHomeScreen = () => {
    return userType === 'REPARTIDOR' ? RepartidorHomeScreen : ClienteHomeScreen;
  };

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Login">
        <Stack.Screen 
          name="Login" 
          component={LoginScreen}
          options={{ headerShown: false }}
        />
        <Stack.Screen 
          name="Registro" 
          component={RegistroScreen}
          options={{ title: 'Registro' }}
        />
        <Stack.Screen 
          name="Home" 
          component={getHomeScreen()}
          options={{ headerShown: false }}
        />
        <Stack.Screen 
          name="Perfil" 
          component={PerfilScreen}
          options={{ title: 'Mi Perfil' }}
        />
        <Stack.Screen 
          name="CrearPedido" 
          component={CrearPedidoScreen}
          options={{ title: 'Crear Pedido' }}
        />
        <Stack.Screen 
          name="DetallePedido" 
          component={DetallePedidoScreen}
          options={{ title: 'Detalle del Pedido' }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;