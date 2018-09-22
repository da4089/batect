/*
   Copyright 2017-2018 Charles Korn.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package batect.docker.pull

class DockerRegistryCredentialsProvider(
    private val domainResolver: DockerRegistryDomainResolver,
    private val indexResolver: DockerRegistryIndexResolver,
    private val configurationFile: DockerRegistryCredentialsConfigurationFile
) {
    fun getCredentials(imageName: String): DockerRegistryCredentials? {
        val domain = domainResolver.resolveDomainForImage(imageName)
        val index = indexResolver.resolveRegistryIndex(domain)
        val source = configurationFile.getCredentialsForRegistry(index)

        if (source == null) {
            return null
        }

        return source.load()
    }
}