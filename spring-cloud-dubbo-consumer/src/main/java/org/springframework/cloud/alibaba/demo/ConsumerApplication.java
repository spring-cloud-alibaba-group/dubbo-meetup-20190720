/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.alibaba.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.dubbo.annotation.DubboTransported;
import org.springframework.cloud.alibaba.service.User;
import org.springframework.cloud.alibaba.service.UserService;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ConsumerApplication {

    @Reference(version = "1.0.0")
    private UserService userService;

    @Autowired
    private DubboFeignUserService dubboFeignUserService;

    @Autowired
    private FeignUserService feignUserService;

    @FeignClient("${provider.application.name}")
    @DubboTransported(protocol = "dubbo")
    interface DubboFeignUserService {
        @GetMapping("/user/save")
        User save(@RequestParam("name") String name, @RequestParam("age") Integer age);
    }

    @FeignClient("${provider.application.name}")
    interface FeignUserService {
        @GetMapping("/user/save")
        User save(@RequestParam("name") String name, @RequestParam("age") Integer age);
    }

    @Bean
    public ApplicationRunner userServiceRunner() {
        return arguments -> {
            User dubboUser = userService.save("dubbo-user", 10);
            User feignUser = feignUserService.save("feign-user", 20);
            User dubboFeignUser = dubboFeignUserService.save("dubbofeign-user", 30);
            System.out.println(dubboUser);
            System.out.println(feignUser);
            System.out.println(dubboFeignUser);
        };
    }

    @RestController
    class TestController {
        @GetMapping("/test")
        public List<User> test() {
            List<User> result = new ArrayList<>();
            result.add(userService.save("dubbo-user", 10));
            result.add(feignUserService.save("feign-user", 20));
            result.add(dubboFeignUserService.save("dubbofeign-user", 30));
            return result;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
