#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { WebappStack } from '../lib/webapp-stack';

const app = new cdk.App();
new WebappStack(app, 'WebappStack', {
  env: { account: '435481488684', region: 'us-east-1' },
});