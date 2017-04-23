# decompose

## MVP TODO

### Config file handling
* validate configuration (eg. containers referenced in tasks as dependencies and direct targets must exist, build directory must exist)
* allow configuration includes (ie. allow splitting the configuration over multiple files)
* better error message when a key (eg. a task name) is used twice (at the moment it's `Duplicate field 'duplicated_task_name'`)
* allow tasks with just containers to start (ie. no `run` entry)

### Features
* return code options (any non-zero, particular container, first to exit)
* logging options (all or particular container) - will this be implied by the presence of a `run` configuration?
* exit options (close all after any container stops, wait for all to stop)
* use an existing image, pulling if necessary
* dependencies between containers
* requires / provides relationships
* running multiple containers at once
* creating an isolated network for all containers to use
* don't require command to be specified for each container in each task (allow a default to be set in the container's configuration)
* allow the user to keep containers after failure so they can examine logs
* some way to see a list of available tasks
* pass-through additional command line arguments to a `run`

### Other
* parse command lines properly (for command line when starting container)

## Future improvements
* handle expanded form of mappings, for example:
  
  ```yaml
  containers:
    build-env:
      build_dir: build-env
      environment:
        - name: THING
          value: thing_value
  
  ```

* wildcard includes
* deal with alternative volume mount specifications (eg. 'ro')
* support port ranges in mappings
* support protocols other than TCP in port mappings
* shell tab completion for tasks (eg. `decompose run b<tab>` completes to `decompose run build`)
