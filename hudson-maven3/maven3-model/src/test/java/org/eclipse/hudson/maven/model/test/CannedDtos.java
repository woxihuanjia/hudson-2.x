/**
 * The MIT License
 *
 * Copyright (c) 2010-2011 Sonatype, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.eclipse.hudson.maven.model.test;

import org.eclipse.hudson.maven.model.MavenCoordinatesDTO;
import org.eclipse.hudson.maven.model.state.ArtifactDTO;

/**
 * Various DTOs created in a known state for use in testing.
 * 
 * @author Jamie Whitehouse
 */
public class CannedDtos
{
    private CannedDtos() {
        // non-instantiable
    }

    public static ArtifactDTO fakeArtifact() {
        return new ArtifactDTO().withCoordinates(fakeCoordinates());
    }

    public static MavenCoordinatesDTO fakeCoordinates() {
        return new MavenCoordinatesDTO()
            .withGroupId("fake-groupId")
            .withArtifactId("fake-artifactId")
            .withType("fake-extension")
            .withVersion("1.0-SNAPSHOT")
            .withClassifier("fake-classifier")
            .withExpandedMetaVersion("1.0-SNAPSHOT");
    }
    
    public static MavenCoordinatesDTO fakeCoordinates(final String artifactId)
    {
        return fakeCoordinates().withArtifactId(artifactId).normalize();
    }
}