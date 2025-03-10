/*
 * Copyright 2012-2022 the original author or authors.
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

package smoketest.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
@EnableBatchProcessing
public class SampleBatchApplication {

	private JobBuilderFactory jobs;

	private StepBuilderFactory steps;

	private PlatformTransactionManager transactionManager;

	public SampleBatchApplication(JobBuilderFactory jobs, StepBuilderFactory steps,
			PlatformTransactionManager transactionManager) {
		this.jobs = jobs;
		this.steps = steps;
		this.transactionManager = transactionManager;
	}

	@Bean
	protected Tasklet tasklet() {
		return (contribution, context) -> RepeatStatus.FINISHED;
	}

	@Bean
	public Job job() {
		return this.jobs.get("job").start(step1()).build();
	}

	@Bean
	protected Step step1() {
		return this.steps.get("step1").tasklet(tasklet()).transactionManager(this.transactionManager).build();
	}

	public static void main(String[] args) {
		// System.exit is common for Batch applications since the exit code can be used to
		// drive a workflow
		System.exit(SpringApplication.exit(SpringApplication.run(SampleBatchApplication.class, args)));
	}

}
