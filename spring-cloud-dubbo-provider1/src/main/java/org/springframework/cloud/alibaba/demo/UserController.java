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

import java.util.UUID;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.cloud.alibaba.service.User;
import org.springframework.cloud.alibaba.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.cloud.alibaba.service.LoggerUtils.log;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@RestController
@Service(version = "1.0.0")
public class UserController implements UserService {

    @Override
    @GetMapping("/user/save")
    public User save(@RequestParam String name, @RequestParam Integer age) {
        log("provider1 /user/save", name + "-" + age);
        return new User("provider1-" + UUID.randomUUID().toString(), name, age);
    }

}
