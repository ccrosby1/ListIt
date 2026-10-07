/**
 * Milestone - Inventory Service Configuration
 * 
 * This class is responsible for configuring the InventoryService bean.
 * It specifies the initialization and destruction methods for the bean.
 */
package com.gcu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.business.InventoryService;
import com.gcu.business.InventoryServiceInterface;

/**
 * Configuration class for the Inventory Service.
 * This class defines the beans and their lifecycle methods.
 */
@Configuration
public class MileConfig {

	/**
	 * This method creates a bean of type InventoryServiceInterface.
	 * It specifies the initialization and destruction methods for the bean.
	 * 
	 * @return an instance of InventoryServiceInterface
	 */
	@Bean(name="inventoryService", initMethod="init", destroyMethod="destroy")
	InventoryServiceInterface inventoryService() {
		return new InventoryService();
	}
}
